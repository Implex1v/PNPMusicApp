import Head from 'next/head'
import Navbar from "./Navbar";

export default function Layout({children, title=""}) {
    return (
        <>
            <Head>
                <meta
                    name="description"
                    content="A website for RPG/PnP music playing"
                />
                <meta name="og:title" content={title}/>
                <meta name="twitter:card" content="summary_large_image"/>
                <title>{title}</title>
            </Head>
            <div>
                <div className="layout">
                    <div className="main container">
                        <div className="bg-dark text-light">
                            <Navbar />
                            <div className="row p-2">
                                <div className="col-xl-12">
                                    <main className="mb-auto">
                                        {children}
                                    </main>
                                </div>
                            </div>

                            <footer className="row mt-5 mb-5">
                                <div className="col-md-12">
                                    <hr />
                                    <div className="w-full mx-auto">
                                        <p className="text-center">
                                            &copy; PnPMusicApp
                                        </p>
                                    </div>
                                </div>
                            </footer>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}